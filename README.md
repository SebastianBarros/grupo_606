# grupo_606
TP Android - 2020 - Unlam

Integrantes
•	Barros, Sebastián 	DNI: 39.272.364 
•	Costa, Lautaro 	DNI: 36.396.983

Grupo: 606.

La aplicación se encuentra divida en 3 pantallas:
Login, registro y el menú principal.
Se verifica la conexión a internet (tanto wifi como red de datos) antes de intentar un llamado a la api de la universidad.
El menú principal muestra 2 sensores: proximidad y luz. Para cada sensor se muestra el valor actual (con mayor tamaño) y los 2 últimos. Así mismo pueden tocarse los botones de Registrar para enviar a la api la información de cada sensor. Por último, hay un proceso corriendo en el background que envía un evento a la api cada minuto que la app se encuentra en el menú principal.
