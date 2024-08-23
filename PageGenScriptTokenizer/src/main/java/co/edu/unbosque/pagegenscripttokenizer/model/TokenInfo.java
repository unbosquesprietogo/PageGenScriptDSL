package co.edu.unbosque.pagegenscripttokenizer.model;

import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.regex.Pattern;

@Getter
@AllArgsConstructor
public class TokenInfo {
    private final Pattern regex;
    private final int token;
}
