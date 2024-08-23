package co.edu.unbosque.pagegenscripttokenizer.service;

import co.edu.unbosque.pagegenscripttokenizer.model.Token;
import co.edu.unbosque.pagegenscripttokenizer.model.TokenInfo;

import java.util.List;

public interface TokenService {

    List<TokenInfo> getTokenInfos();  // Método para obtener tokenInfos

    List<Token> getTokens();  // Método para obtener tokens

    void add(String regex, int token);

    void tokenize(String str);

    String removeComments(String code);
}
