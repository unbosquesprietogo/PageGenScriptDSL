from rest_framework.decorators import api_view
from rest_framework.response import Response
from pageGenScript.services.token_service import TokenService
from pageGenScript.serializers import TokenizeRequestSerializer
from pageGenScript.exceptions import LexerException
from pageGenScript.models import Token

@api_view(['POST'])
def tokenize(request):
    # Deserializar la solicitud
    serializer = TokenizeRequestSerializer(data=request.data)
    serializer.is_valid(raise_exception=True)

    tokenize_request = serializer.validated_data
    token_service = TokenService()

    # Configura los tokens que tu analizador léxico debería reconocer
    token_service.add(r'page', Token.PAGE)
    token_service.add(r'title', Token.TITLE)
    token_service.add(r'styles', Token.STYLES)
    token_service.add(r'primary-color', Token.PRIMARY_COLOR)
    token_service.add(r'secondary-color', Token.SECONDARY_COLOR)
    token_service.add(r'sections', Token.SECTIONS)
    token_service.add(r'type', Token.TYPE)
    token_service.add(r'navbar', Token.NAVBAR)
    token_service.add(r'background-color', Token.BACKGROUND_COLOR)
    token_service.add(r'items', Token.ITEMS)
    token_service.add(r'text', Token.TEXT)
    token_service.add(r'link', Token.LINK)
    token_service.add(r'color', Token.COLOR)
    token_service.add(r'hero', Token.HERO)
    token_service.add(r'subtitle', Token.SUBTITLE)
    token_service.add(r'button', Token.BUTTON)
    token_service.add(r'card-deck', Token.CARD_DECK)
    token_service.add(r'cards', Token.CARDS)
    token_service.add(r'card', Token.CARD)
    token_service.add(r'accordion', Token.ACCORDION)
    token_service.add(r'content', Token.CONTENT)
    token_service.add(r'alerts', Token.ALERTS)
    token_service.add(r'alert', Token.ALERT)
    token_service.add(r'success', Token.SUCCESS)
    token_service.add(r'danger', Token.DANGER)
    token_service.add(r'message', Token.MESSAGE)
    token_service.add(r'modal', Token.MODAL)
    token_service.add(r'id', Token.ID)
    token_service.add(r'exampleModal', Token.EXAMPLE_MODAL)
    token_service.add(r'title', Token.TITLE_MODAL)
    token_service.add(r'body', Token.BODY)
    token_service.add(r'carousel', Token.CAROUSEL)
    token_service.add(r'image', Token.IMAGE)
    token_service.add(r'caption', Token.CAPTION)
    token_service.add(r'list-group', Token.LIST_GROUP)
    token_service.add(r'toast', Token.TOAST)
    token_service.add(r'toastExample', Token.TOAST_EXAMPLE)
    token_service.add(r'delay', Token.DELAY)
    token_service.add(r"'[^']*'", Token.STRING)  # Literales de cadena
    token_service.add(r'\d+', Token.NUMBER)  # Números
    token_service.add(r'[\[\],:]', Token.SYMBOL)  # Símbolos
    token_service.add(r'-', Token.MINUS)
    token_service.add(r':', Token.COLON)

    try:
        token_service.tokenize(tokenize_request['dsl_content'])
        return Response(token_service.get_tokens())
    except LexerException as e:
        return Response({'error': str(e)}, status=400)
