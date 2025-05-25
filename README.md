# 🌌 Planets - Explorador del Sistema Solar

**Planets** es una aplicación Android que permite a los usuarios explorar información sobre los planetas del sistema solar. La app obtiene los datos desde una API creada en [Apiary](https://apiary.io/), e incluye imágenes y descripciones.

## 🚀 Características

- Pantalla de **splash** con temática espacial al iniciar la app.
- **Consumo de API REST** con Retrofit usando endpoints definidos en Apiary.
- Pantalla principal con una lista de planetas que incluye:
  - Imagen
  - Nombre
- Al seleccionar un planeta, se accede a una pantalla con **detalles completos**:
  - Distancia al Sol
  - Diámetro
  - Cantidad de lunas
  - Presencia de anillos
  - Posición en el sistema solar
  - **Video educativo embebido desde YouTube**
  - **Reproducción de sonido local** distintivo por planeta
- Botón de pantalla completa que permite ver el video horizontalmente en una nueva actividad.
- El sonido local se detiene automáticamente cuando inicia el video.
- **Animación de carga** con frases aleatorias y divertidas.
- **Bloqueo de rotación global** excepto en la pantalla de video.
- Manejo de errores con mensajes claros y amigables.



## 🧪 Tecnologías utilizadas

- Kotlin
- Android SDK (API 24+)
- ViewModel + LiveData
- ViewBinding
- Retrofit + GSON
- Glide
- [Android YouTube Player API (modern)](https://github.com/PierfrancescoSoffritti/android-youtube-player)


## 🖼️ Capturas de pantalla

<p align="center">
  <img src="https://github.com/user-attachments/assets/f295290b-2e42-4d00-9864-08b6bd721f48" width="30%">
  <img src="https://github.com/user-attachments/assets/a20e6784-95f5-455f-82bc-fa9d65a52891" width="30%">
  <img src="https://github.com/user-attachments/assets/f7bdf172-ff12-4dc9-a329-172def1a0c20" width="30%">
</p>


