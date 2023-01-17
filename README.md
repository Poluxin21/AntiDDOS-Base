# Example-AntiDDOS
Base de Anti-DDOS ( EM DESEVOLVIMENTO )

Os arquivos tem:
- Um server 
- Um client para executar testes locais

# Instalação - Linux

``` 
sudo git clone https://github.com/Poluxin21/AntiDDOS-Base
sudo apt-get install openjdk-8-headless
sudo apt-get upgrade && update
cd AntiDDOS-Base
``` 

# Inicialização

Inicie sempre o Server antes do cliente

- SERVIDOR

```
$ mvn clean install
$ mvn package or mvn install
$ mvn exec:java or java -jar Project-1.0-SNAPSHOT.jar
```
Para testes

```
$ javac Client.java
$ java Client
```

# Instalação - Windows

- Baixe o openjdk8 em: https://www.oracle.com/br/java/technologies/javase/javase8u211-later-archive-downloads.html]
- instale o openjdk 

# Instalação Maven - Windows
- Faça a instalação do maven aqui: https://maven.apache.org/download.cgi
- Você pode seguir os passos de configuração aqui: https://dicasdejava.com.br/como-instalar-o-maven-no-windows/

```
$ mvn clean install
$ mvn clean compile
$ mvn exec:java or java -jar Project-1.0-SNAPSHOT.jar
```
