FROM nginx:alpine

RUN rm -rf /usr/share/nginx/html/*

# Copiar archivos de la aplicación
COPY . /usr/share/nginx/html

# Copiar configuración personalizada de Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
