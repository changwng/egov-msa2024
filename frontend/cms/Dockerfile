# cms
FROM node:14.8.0-alpine

ENV APP_HOME=/usr/app/
RUN mkdir -p ${APP_HOME}
WORKDIR $APP_HOME

COPY package*.json .
RUN npm install
COPY . .
RUN npm run build

CMD ["npm", "run", "start"]
