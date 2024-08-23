from rest_framework import serializers

class TokenizeRequestSerializer(serializers.Serializer):
    dsl_content = serializers.CharField()
