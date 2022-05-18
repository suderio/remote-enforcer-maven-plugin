# remote-enforcer-maven-plugin

## Introdução

Plugin para verificar a existência de dependências remotas. Por enquanto apenas links acessadas através de http/https estão implementados.
## Utilização
O plugin pode ser configurado na seção build do pom. O exemplo abaixo mostra como configurar para verificar a existência de serviços no ambiente dsv.

```

		<plugin>
			<groupId>net.technearts</groupId>
			<artifactId>remote-dependency-maven-plugin</artifactId>
			<version>1.0.0</version>
			<executions>
				<execution>
					<id>check</id>
					<inherited>false</inherited>
					<goals>
						<goal>check</goal>
					</goals>
				</execution>
			</executions>
			<configuration>
				<resources>
					<xxx>https://localhost/sss</xxx>
				</resources>
			</configuration>
		</plugin>

```

A seção `<configuration>` deve indicar, em `<resources>` os links que devem estar presentes.

## Resultado

O resultado da execução é uma mensagem por dependência configurada, indicando se o serviço existe no ambiente ou não:


```

[WARNING] xxx is not OK
[INFO] yyy is OK

```

## Exemplo

Vamos utilizar o projeto Portal de Negócios como exemplo. O projeto depende, entre outros, dos serviços xxx e yyy, e possui um parent pom em zzz_src/zzz_parent. Nesse pom iremos acrescentar a seguinte configuração de build:

```
  <build>
    <plugins>
    
      ...
      
      <plugin>
        <groupId>net.technearts</groupId>
        <artifactId>remote-dependency-maven-plugin</artifactId>
        <version>1.0.0</version>
        <executions>
          <execution>
            <id>check</id>
            <inherited>false</inherited>
            <goals>
              <goal>check</goal>
            </goals>
          </execution>
        </executions>
        <configuration>
          <resources>
            <xxx>${serviceURIPrefix}/xxx/sca/xxx</CNAEPesquisar>
            <yyy>${serviceURIPrefix}/yyy/sca/yyy</yyy>
          </resources>
        </configuration>
      </plugin>
    </plugins>
  </build>
```

E os seguintes perfis:

```

  <profile>
      <id>sbx</id>
      <properties>
        <serviceURIPrefix>http://localhost</serviceURIPrefix>
      </properties>
  </profile>
  <profile>
    <id>dsv</id>
    <activation>
      <activeByDefault>true</activeByDefault>
    </activation>
    <properties>
      <serviceURIPrefix>https://localhost:443</serviceURIPrefix>
    </properties>
  </profile>
  <profile>
    <id>tst</id>
    <properties>
      <serviceURIPrefix>https://localhost:443</serviceURIPrefix>
    </properties>
  </profile>

```

Os perfis criam as variáveis com a URL base dos serviços em cada ambiente, enquanto a configuração do plugin registra dois serviços dos quais o projeto depende (xxx e yyy). Ao executar a fase `validate` o maven irá executar o plugin e informar se o serviço está disponível no ambiente correspondente ao perfil selecionado. O resultado, supondo que um dos serviços exista no ambiente especificado e o outro não, seria:

```
[INFO] xxx is OK
[INFO] yyy is OK
```

 
