package com.example.library_management.service.impl;

import com.example.library_management.dto.BookRequest;
import com.example.library_management.dto.BookResponse;
import com.example.library_management.dto.DtoConverter;
import com.example.library_management.entity.*;
import com.example.library_management.exception.ResourceNotFoundException;
import com.example.library_management.repo.*;
import com.example.library_management.service.IBookService;
import io.jsonwebtoken.io.IOException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements IBookService {

    private final BookRepo bookRepo;
    private final AuthorRepo authorRepo;
    private final GenreRepo genreRepo;
    private final PublisherRepo publisherRepo;

    private Book findEntityById(int id) {
        return bookRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    @Override
    public BookResponse getById(int id) {
        return DtoConverter.toDto(findEntityById(id));
    }

    @Override
    public Page<BookResponse> findAll(Pageable pageable) {
        return bookRepo.findAll(pageable).map(DtoConverter::toDto);
    }

    @Transactional
    @Override
    public BookResponse addBook(BookRequest request) {
        Book book = new Book();
        book.setName(request.getName());
        book.setAuthors(authorRepo.findAllById(request.getAuthorIds()));
        book.setGenres(genreRepo.findAllById(request.getGenreIds()));
        book.setPublishers(publisherRepo.findAllById(request.getPublisherIds()));

        return DtoConverter.toDto(bookRepo.save(book));
    }

    @Transactional
    @Override
    public BookResponse updateBook(int id, BookRequest request) {
        Book existing = findEntityById(id);
        existing.setName(request.getName());
        existing.setAuthors(authorRepo.findAllById(request.getAuthorIds()));
        existing.setGenres(genreRepo.findAllById(request.getGenreIds()));
        existing.setPublishers(publisherRepo.findAllById(request.getPublisherIds()));

        return DtoConverter.toDto(bookRepo.save(existing));
    }

    @Transactional
    @Override
    public void deleteBook(int id) {
        if (!bookRepo.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        bookRepo.deleteById(id);
    }

    @Override
    public Page<BookResponse> filterBooks(String name, String author, String genre, String publisher, Pageable pageable) {
        Page<Book> books = bookRepo.findAll(pageable);

        List<Book> filtered = books.getContent().stream()
                .filter(book -> isNullOrContainsIgnoreCase(book.getName(), name))
                .filter(book -> isNullOrAnyMatch(book.getAuthors(), author))
                .filter(book -> isNullOrAnyMatch(book.getGenres(), genre))
                .filter(book -> isNullOrAnyMatch(book.getPublishers(), publisher))
                .toList();

        List<BookResponse> responseList = filtered.stream().map(DtoConverter::toDto).toList();
        return new PageImpl<>(responseList, pageable, responseList.size());
    }

    private boolean isNullOrContainsIgnoreCase(String field, String keyword) {
        return keyword == null || (field != null && field.toLowerCase().contains(keyword.toLowerCase()));
    }

    private <T> boolean isNullOrAnyMatch(List<T> list, String keyword) {
        if (keyword == null) return true;
        return list.stream().anyMatch(item -> {
            if (item instanceof Author a) return a.getName().toLowerCase().contains(keyword.toLowerCase());
            if (item instanceof Genre g) return g.getName().toLowerCase().contains(keyword.toLowerCase());
            if (item instanceof Publisher p) return p.getName().toLowerCase().contains(keyword.toLowerCase());
            return false;
        });
    }

    @Override
    public ByteArrayInputStream exportBooksToExcel() throws IOException {
        List<Book> books = bookRepo.findAll();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Books");
            String[] headers = {"ID", "Name", "Authors", "Genres", "Publishers", "Status"};

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            int rowIdx = 1;
            for (Book book : books) {
                Row row = sheet.createRow(rowIdx++);

                String authors = book.getAuthors().stream().map(Author::getName).collect(Collectors.joining(", "));
                String genres = book.getGenres().stream().map(Genre::getName).collect(Collectors.joining(", "));
                String publishers = book.getPublishers().stream().map(Publisher::getName).collect(Collectors.joining(", "));

                String status;
                boolean isLost = book.getLossRecords() != null && !book.getLossRecords().isEmpty();
                boolean isBorrowed = book.getLoans().stream().anyMatch(loan -> !loan.isReturned());
                if (isLost) status = "Lost";
                else if (isBorrowed) status = "Borrowed";
                else status = "Available";

                row.createCell(0).setCellValue(book.getId());
                row.createCell(1).setCellValue(book.getName());
                row.createCell(2).setCellValue(authors);
                row.createCell(3).setCellValue(genres);
                row.createCell(4).setCellValue(publishers);
                row.createCell(5).setCellValue(status);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public ResponseEntity<byte[]> getBooksExcelResponse() throws IOException {
        byte[] fileContent = exportBooksToExcel().readAllBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.attachment().filename("books.xlsx").build());

        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    }
}

