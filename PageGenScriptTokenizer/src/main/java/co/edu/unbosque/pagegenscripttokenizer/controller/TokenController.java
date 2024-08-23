package co.edu.unbosque.pagegenscripttokenizer.controller;

import co.edu.unbosque.pagegenscripttokenizer.model.Token;
import co.edu.unbosque.pagegenscripttokenizer.model.TokenizeRequest;
import co.edu.unbosque.pagegenscripttokenizer.service.TokenService;
import co.edu.unbosque.pagegenscripttokenizer.service.impl.TokenServiceImpl;
import co.edu.unbosque.pagegenscripttokenizer.exception.LexerException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tokenizer")
public class TokenController {

    @CrossOrigin("*")
    @PostMapping("/tokenize")
    public List<Token> tokenize(@RequestBody TokenizeRequest tokenizeRequest) {
        TokenService tokenService = new TokenServiceImpl();

        // Configura los tokens que tu analizador léxico debería reconocer
        tokenService.add("page", Token.PAGE);
        tokenService.add("title", Token.TITLE);
        tokenService.add("styles", Token.STYLES);
        tokenService.add("primary-color", Token.PRIMARY_COLOR);
        tokenService.add("secondary-color", Token.SECONDARY_COLOR);
        tokenService.add("sections", Token.SECTIONS);
        tokenService.add("type", Token.TYPE);
        tokenService.add("navbar", Token.NAVBAR);
        tokenService.add("background-color", Token.BACKGROUND_COLOR);
        tokenService.add("items", Token.ITEMS);
        tokenService.add("text", Token.TEXT);
        tokenService.add("link", Token.LINK);
        tokenService.add("color", Token.COLOR);
        tokenService.add("hero", Token.HERO);
        tokenService.add("subtitle", Token.SUBTITLE);
        tokenService.add("button", Token.BUTTON);
        tokenService.add("card-deck", Token.CARD_DECK);
        tokenService.add("cards", Token.CARDS);
        tokenService.add("card", Token.CARD);
        tokenService.add("accordion", Token.ACCORDION);
        tokenService.add("content", Token.CONTENT);
        tokenService.add("alerts", Token.ALERTS);
        tokenService.add("alert", Token.ALERT);
        tokenService.add("success", Token.SUCCESS);
        tokenService.add("danger", Token.DANGER);
        tokenService.add("message", Token.MESSAGE);
        tokenService.add("modal", Token.MODAL);
        tokenService.add("id", Token.ID);
        tokenService.add("exampleModal", Token.EXAMPLE_MODAL);
        tokenService.add("title", Token.TITLE_MODAL);
        tokenService.add("body", Token.BODY);
        tokenService.add("carousel", Token.CAROUSEL);
        tokenService.add("image", Token.IMAGE);
        tokenService.add("caption", Token.CAPTION);
        tokenService.add("list-group", Token.LIST_GROUP);
        tokenService.add("toast", Token.TOAST);
        tokenService.add("toastExample", Token.TOAST_EXAMPLE);
        tokenService.add("delay", Token.DELAY);
        tokenService.add("'[^']*'", Token.STRING); // Literales de cadena
        tokenService.add("\\d+", Token.NUMBER); // Números
        tokenService.add("[\\[\\],:]", Token.SYMBOL); // Símbolos
        tokenService.add("-", Token.MINUS);
        tokenService.add(":", Token.COLON);

        System.out.println(tokenizeRequest.getDslContent());

        try {
            tokenService.tokenize(tokenizeRequest.getDslContent());
            return tokenService.getTokens();
        } catch (LexerException e) {
            throw new RuntimeException("Error during tokenization: " + e.getMessage(), e);
        }
    }
}
