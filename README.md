# FrodyAiBot

Esta es una aplicación de chatbot que se conecta a la API de Hugging Face y utiliza el modelo de lenguaje Mistrax para responder preguntas del usuario.

## Instalación

La instalación es muy sencilla, descargamos el codigo en nuestro local. Solamente tendremos que modificar el archivo `application.yml` con los datos del bot de Hugging Face y el bot de telegram.

```bash
huggingface:
  api-key: "{NUESTRO_API_KEY}"
  model: "{Modelo elegido, recomiendo mistralai/Mixtral-8x7B-Instruct-v0.1}"

telegram:
  bot:
    token: "{Token de nuestro bot de telegram}"
    username: "{Nombre de nuestro bot de telegram}"
```

Una vez que tengamos estos datos, arrancamos la aplicación y escribimos al bot de telegram.

## Configuración del Bot de Telegram

1. Crear un bot de telegram con el bot de `@BotFather` y le escribimos `/newbot` para crear un nuevo bot.
2. Nos pedirá un nombre para el bot.
3. Nos pedirá un nombre de usuario para el bot. Este es el que tenemos que poner en username en el archivo `application.yml`.
4. Nos dará un token que tenemos que poner en el archivo `application.yml`.

## Configuración de la IA en Hugging Face

1. Nos registramos en la web de Hugging Face.
2. Nos vamos a la sección de `Settings` y nos vamos a la sección de `API Keys`.
3. Creamos una nueva API Key y la ponemos en el archivo `application.yml`.
4. Vamos a la sección de `Models` y buscamos el modelo que queremos utilizar. En este caso, recomiendo `mistralai/Mixtral-8x7B-Instruct-v0.1`.
5. Ponemos el nombre del modelo en el archivo `application.yml`.
