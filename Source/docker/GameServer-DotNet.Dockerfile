FROM mcr.microsoft.com/dotnet/sdk:6.0

WORKDIR /game_server

ADD ./server/rabbit-game/rabbit-game.csproj ./
RUN dotnet restore

ADD ./server/rabbit-game/src ./src
ADD ./server/rabbit-game/appsettings.Production.json ./
ADD ./server/rabbit-game/appsettings.json ./
RUN dotnet publish --output ./publish_output

WORKDIR ./publish_output

ENV DOTNET_ENVIRONMENT Production

ENTRYPOINT ["dotnet","rabbit-game.dll"]



