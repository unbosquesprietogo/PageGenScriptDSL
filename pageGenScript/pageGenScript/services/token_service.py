import re
from pageGenScript.exceptions import LexerException

class TokenService:
    def __init__(self):
        self.token_infos = []
        self.tokens = []

    def add(self, regex, token):
        self.token_infos.append({'regex': re.compile(f"^{regex}"), 'token': token})

    def remove_comments(self, code):
        # Primero eliminamos los comentarios multilínea
        no_multi_line_comments = self.remove_multi_line_comments(code)
        # Luego eliminamos los comentarios de una sola línea
        return self.remove_single_line_comments(no_multi_line_comments)

    def remove_multi_line_comments(self, code):
        # Patrón para comentarios multilínea (entre triples comillas)
        multi_line_comment_pattern = r"(?s)'''(.*?)'''|\"\"\"(.*?)\"\"\""
        # Reemplazamos comentarios multilínea con un marcador temporal
        return re.sub(multi_line_comment_pattern, '/*MULTILINE_COMMENT*/', code)

    def remove_single_line_comments(self, code):
        # Patrón para comentarios de una sola línea (precedidos por #)
        # Considera los comentarios dentro de cadenas usando el marcador temporal
        single_line_comment_pattern = r'(?<![\'\"#])#.*'
        # Reemplazamos los comentarios de una sola línea con una cadena vacía
        without_single_line_comments = re.sub(single_line_comment_pattern, '', code)
        # Restaurar los comentarios multilínea eliminados
        return without_single_line_comments.replace('/*MULTILINE_COMMENT*/', '')

    def tokenize(self, s):
        s = self.remove_comments(s).strip()
        total_length = len(s)
        self.tokens.clear()

        while s:
            match = False
            for info in self.token_infos:
                m = info['regex'].match(s)
                if m:
                    match = True
                    tok = m.group().strip()
                    s = s[m.end():].strip()
                    self.tokens.append({
                        'token': info['token'],
                        'lexeme': tok,
                        'pos': total_length - len(s)
                    })
                    break

            if not match:
                raise LexerException(f"Unexpected character in input: {s}")

    def get_tokens(self):
        return self.tokens
