package com.example.library_management.dto;

import com.example.library_management.entity.*;

import java.util.List;
import java.util.stream.Collectors;

public class DtoConverter {
    private DtoConverter() {
    }


    public static UserResponse toDto(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setEmail(user.getEmail());
        userResponse.setRoleId(user.getRole().getId());
        userResponse.setUsername(user.getUsername());
        return userResponse;
    }


    public static RoleResponse toDto(Role role) {
        RoleResponse roleResponse = new RoleResponse();
        roleResponse.setId(role.getId());
        roleResponse.setName(role.getName());
        return roleResponse;
    }

    public static User toEntity(UserRequest request, Role role) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setRole(role);
        return user;
    }
    public static LoanResponse toDto(Loan loan) {
        return LoanResponse.builder()
                .id(loan.getId())
                .bookId(loan.getBook().getId())
                .bookName(loan.getBook().getName())
                .userId(loan.getUser().getId())
                .username(loan.getUser().getUsername())
                .loanDate(loan.getLoanDate())
                .returnDate(loan.getReturnDate())
                .returned(loan.isReturned())
                .build();
    }
    public static List<LoanResponse> toLoanDtoList(List<Loan> loans) {
        return loans.stream()
                .map(DtoConverter::toDto)
                .collect(Collectors.toList());
    }
    public static BookResponse toDto(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .name(book.getName())
                .authors(book.getAuthors() == null ? List.of() : book.getAuthors().stream()
                        .map(Author::getName).toList())
                .genres(book.getGenres() == null ? List.of() : book.getGenres().stream()
                        .map(Genre::getName).toList())
                .publishers(book.getPublishers() == null ? List.of() : book.getPublishers().stream()
                        .map(Publisher::getName).toList())
                .build();
    }
    public static AuthorResponse toDto(Author author) {
        return AuthorResponse.builder()
                .id(author.getId())
                .name(author.getName())
                .build();
    }

    public static GenreResponse toDto(Genre genre) {
        return GenreResponse.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }
    public static PublisherResponse toDto(Publisher publisher) {
        return PublisherResponse.builder()
                .id(publisher.getId())
                .name(publisher.getName())
                .build();
    }

}

