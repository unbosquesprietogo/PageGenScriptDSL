from django.db import models

class Token(models.Model):
    PAGE = 0
    TITLE = 1
    STYLES = 2
    PRIMARY_COLOR = 3
    SECONDARY_COLOR = 4
    SECTIONS = 5
    TYPE = 6
    NAVBAR = 7
    BACKGROUND_COLOR = 8
    ITEMS = 9
    TEXT = 10
    LINK = 11
    COLOR = 12
    HERO = 13
    SUBTITLE = 14
    BUTTON = 15
    CARD_DECK = 16
    CARDS = 17
    CARD = 18
    ACCORDION = 19
    CONTENT = 20
    ALERTS = 21
    ALERT = 22
    SUCCESS = 23
    DANGER = 24
    MESSAGE = 25
    MODAL = 26
    ID = 27
    EXAMPLE_MODAL = 28
    TITLE_MODAL = 29
    BODY = 30
    CAROUSEL = 31
    IMAGE = 32
    CAPTION = 33
    LIST_GROUP = 34
    TOAST = 35
    TOAST_EXAMPLE = 36
    DELAY = 37
    STRING = 38
    NUMBER = 39
    SYMBOL = 40
    MINUS = 41
    COLON = 42

    token = models.IntegerField()
    lexeme = models.CharField(max_length=255)
    pos = models.IntegerField()

    def __str__(self):
        return f"[Token: {self.token} Lexeme: {self.lexeme} Position: {self.pos}]"
