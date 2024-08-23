package co.edu.unbosque.pagegenscripttokenizer.service.impl;

import co.edu.unbosque.pagegenscripttokenizer.exception.LexerException;
import co.edu.unbosque.pagegenscripttokenizer.model.Token;
import co.edu.unbosque.pagegenscripttokenizer.model.TokenInfo;
import co.edu.unbosque.pagegenscripttokenizer.service.TokenService;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TokenServiceImpl implements TokenService {
    private final LinkedList<TokenInfo> tokenInfos;
    @Getter
    private LinkedList<Token> tokens;

    public TokenServiceImpl() {
        tokenInfos = new LinkedList<>();
        tokens = new LinkedList<>();
    }

    @Override
    public void add(String regex, int token) {
        tokenInfos.add(new TokenInfo(Pattern.compile("^(" + regex + ")"), token));
    }

    @Override
    public void tokenize(String str) {
        String s = removeComments(str).trim();
        System.out.println("without comments: \n"+s);
        int totalLength = s.length();
        tokens.clear();
        while (!s.isEmpty()) {
            int remaining = s.length();
            boolean match = false;
            for (TokenInfo info : tokenInfos) {
                Matcher m = info.getRegex().matcher(s);
                if (m.find()) {
                    match = true;
                    String tok = m.group().trim();
                    s = m.replaceFirst("").trim();
                    tokens.add(new Token(info.getToken(), tok, totalLength - remaining));
                    break;
                }
            }
            if (!match) {
                throw new LexerException("Unexpected character in input: " + s);
            }
        }
    }
    @Override
    public String removeComments(String pythonCode) {
        // Eliminamos comentarios multilínea primero
        String noMultiLineComments = removeMultiLineComments(pythonCode);

        // Luego, eliminamos los comentarios de una sola línea
        return removeSingleLineComments(noMultiLineComments);
    }

    private String removeMultiLineComments(String code) {
        // Patrón para comentarios multilínea (entre triples comillas)
        String multiLineCommentPattern = "(?s)'''(.*?)'''|\"\"\"(.*?)\"\"\"";
        Pattern pattern = Pattern.compile(multiLineCommentPattern);
        Matcher matcher = pattern.matcher(code);

        // Reemplazamos comentarios multilínea con un marcador temporal
        return matcher.replaceAll("/*MULTILINE_COMMENT*/");
    }

    private String removeSingleLineComments(String code) {
        // Patrón para comentarios de una sola línea (precedidos por #)
        // Considera los comentarios dentro de cadenas usando el marcador temporal
        String singleLineCommentPattern = "(?<![\"'])#.*";

        // Crear un objeto Pattern para comentarios de una sola línea
        Pattern pattern = Pattern.compile(singleLineCommentPattern);
        Matcher matcher = pattern.matcher(code);

        // Reemplazamos los comentarios de una sola línea con una cadena vacía
        String withoutSingleLineComments = matcher.replaceAll("");

        // Restaurar los comentarios multilínea eliminados

        return withoutSingleLineComments.replace("/*MULTILINE_COMMENT*/", "");
    }

    @Override
    public List<TokenInfo> getTokenInfos() {
        return tokenInfos;
    }

    @Override
    public List<Token> getTokens() {
        return tokens;
    }
}
