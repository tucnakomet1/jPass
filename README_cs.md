*English version [here](README.md).*

# Jpass

Jednoduchý správce hesel napsaný v Javě 21 jakožto zápočtový program v rámci předmětu [NPRG013](https://d3s.mff.cuni.cz/teaching/nprg013/) na [MFF UK](https://www.mff.cuni.cz/).

Celý program byl napsán v IDE [IntelliJ IDEA v2023.1.4](https://www.jetbrains.com/idea/) a [zdokumentován](docs/) za pomoci [javadoc](https://docs.oracle.com/javase/8/docs/technotes/tools/windows/javadoc.html).

## Obsah 
- [Funkce/ vlastnosti](#funkce´)
- [Požadavky](#pozadavky)
- [Instalace](#instalace)
   * [Stažení jar souboru z releases](#run)
   * [Sestavení ze zdrojového kódu](#build)
- [Použití](#pouziti)
   * [Klávesové zkratky](#zkratky)
   * [Příklady](#priklady)
- [Použité balíčky](#balicky)
- [Jak to funguje](#works)
   * [Práce s hesly](#pass)
   * [Šifrování a dešifrování souboru s hesly](#crypt)

<a name="funkce"></a>
## Funkce/ vlastnosti

- Ovladatelný z příkazové řádky nebo přes jednoduché uživatelské rozhraní
- Možnost zkopírování jména a hesla příkazem nebo klávesovou zkratkou
- Po zkopírování se vymaže schránka  (defaultně 5s, lze nastavit)
- Databáze šifrovaná algoritmem AES-256 + KDF pro heslo algoritmem Argon2d
- Nabízí generování hesel podle zadané délky hesla
- Upozornění na slabá hesla - Levenshtein na [wordlist](https://github.com/tests-always-included/password-strength/blob/master/data/common-passwords.txt) častých hesel
- V UI režimu se šipkami nahoru/dolu iteruje historií příkazů a ukládá se daný příkaz do schránky
- Po řádném ukončení programu se databáze opět zašifruje (příkazem `exit` nebo `<Ctrl> + D`)

<a name="pozadavky"></a>
## Požadavky

- Java 21.0.2
- Linux
- Maven (pro sestavení ze zdrojového kódu)

<a name="instalace"></a>
## Instalace

<a name="run"></a>
### Stažení jar souboru z releases

Vydání je na [tomto](https://github.com/tucnakomet1/jPass/releases/tag/v0.0.1) odkazu. Nebo lze stáhnout následujícím příkazem:
>  `wget https://github.com/tucnakomet1/jPass/releases/download/v0.0.1/jpass_v0.0.1.tar.gz`

```bash
tar -xvzf jpass_v0.0.1.tar.gz
cd jpass_v0.0.1

chmod +x jpass

./jpass [arg]
```

<a name="build"></a>
### Sestavení ze zdrojového kódu
```bash
git clone https://github.com/tucnakomet1/jPass.git
cd jPass

mvn package

# spusteni programu
java -jar target/pass*.jar
```


<a name="pouziti"></a>
## Použití

Je možná buď UI verze (voláním `./jpass -ui`), nebo pouze z příkazové řádky voláním `./jpass [arg]`. Příkazy jsou jak pro UI, tak pro příkazový režim stejné a jsou uvedeny níže:

```
Usage: [h/help], [v/version], [config], [add], [gen], [rm], [edit], [id], [uname], [ls], [c], [ui], [exit]
  Basic:
	h | help					 Show this text.
	v | version					 Show version information.
	config <path | seconds>		 Replace in config file path to password file or number of seconds.
	add <uname>					 Add username and password.
	gen <uname> <len> [n] 		 Add username and generate random password. Length is necessary. Optional argument 'n' turns off special characters.
	rm <uname | id>				 Delete username and password.
	edit <uname | id> [n|u|p]	 Edit account name <n | name>, username <u | username> or password <p | password>.

  Info:
	id <int> [y]				 Show username and (hidden) password information - to show plaintext password use with argument y. (example 'id 2 y')
	uname <string> [y]			 Show username and (hidden) password information - to show plaintext password use with argument y.
	ls							 Shows the content of the entire pass file (ids, account names and usernames)
	c [u | p] <id>				 Copy to clipboard. Use with argument u=username or p=password and id value. (example 'c u 2')
```

<a name="zkratky"></a>
### Klávesové zkratky
*Funguje pouze v UI módu.*

- `<Ctrl> + B` - Zkopíruje poslední zobrazené *uživatelské jméno* do schránky. *(použijte alespoň jednou `id` nebo `uname` před touto zkratkou)*
- `<Ctrl> + P` - Zkopíruje poslední zobrazené *heslo* do schránky. *(použijte alespoň jednou `id` nebo `uname` před touto zkratkou)*
- `<Ctrl> + D` - Ukončí program. Ekvivalent příkazu `exit`.
- `<PgUp>` - Zkopíruje poslední *použitý příkaz* do schránky. Iteruje historií *nahoru*.
- `<PgDown>` - Zkopíruje poslední *použitý příkaz* do schránky. Iteruje historií *dolu*.

<a name="priklady"></a>
### Příklady

Přidání nových uživatelských údajů:
> `> add Facebook`
```
Enter username: Bob
Enter password: P@sswOrd123
The entropy of the password is: 36.05374780501028 bits
The password is Weak! Crackable by a typical home computer in a week.
This password is, beside entropy, very weak! It's actually a common password - one of top 10000 most used in 2017!
```

Upravení uživatelských údajů: *(v tomto případě upravujeme heslo)*
> `> edit Facebook p`
```
Generate or enter new password (g/e): g
Enter new length of password and if to use specials (in format '<int>, <y|n>'): 26, y
```

Vypíše informace účtu podle konkrétního názvu účtu: *(včetně hesla díky volitelnému argumentu `y`)*
> `> uname Facebook y`
```
Credentials for account Facebook.
Username: 		Bob
Password: 		**|/,qa(5&kIt|oDQ1vmgE#4b&
```

Vypíše informace účtu podle konkrétního id: *(bez hesla - bez argumentu `y`)*
> `> ls 8`

```
Credentials for account Account.
Username: 		Adam
Password: 		****************
```

Zkopíruje uživatelské jméno podle konkrétního id: *(zkopíruje `Adam` do schránky)*
> `> c u 8`

<a name="balicky"></a>
## Použité balíčky

- **[Bouncy Castle](https://mvnrepository.com/artifact/org.bouncycastle/bcpkix-jdk18on)** - knihovna implementující Argon2
- **[JSON In Java](https://mvnrepository.com/artifact/org.json/json)** - knihovna usnadňující práci s `.json` soubory
- **[JNativeHook](https://mvnrepository.com/artifact/com.github.kwhat/jnativehook)** - knihovna obsahující posluchače klávesnice

<a name="works"></a>
## Jak to funguje


1. Po spuštění `jpass` se nejprve načte *config* soubor se všemi jeho parametry a uloží se do proměnných.
*Pokud soubor config soubor neexistuje, program uživatele vyzve k jeho vytvoření.*
2. Uživatel musí zadat heslo (nejprve se ověří shoda hashe) a následně se tímto heslem dešifruje soubor s hesly.
3. Soubor s hesly se dešifruje do paměti (`byte[] data`) a je v ní držen, dokud není program ukončen. Veškeré změny databáze pracují s těmito `data`. Je proto důležité program náležitě ukončit, protože jinak dojde ke ztrátě dat (pokud byla databáze nějak upravována).

<a name="pass"></a>
### Práce s hesly

- **Ověření častých hesel** - při zadání hesla je ověřeno, jestli zadané heslo nespadá mezi jedno z $10~000$ nejčastějších hesel z roku $2017$. Prochází se tedy [tento](https://github.com/tests-always-included/password-strength/blob/master/data/common-passwords.txt) wordlist.
    - Zároveň se kontroluje [Levenshteinova vzdálenost](https://www.wikiwand.com/en/Levenshtein_distance) daného hesla. Pokud je menší než $4$ a nastane shoda, je opět heslo považováno za velmi slabé a uživatel je na to upozorněn.
- **Entropie hesel** - pro každé zadané heslo je spočítána jeho [entropie](https://www.wikiwand.com/en/Entropy_(information_theory)) v bitech. Inspiroval jsem se [tímto](http://tests-always-included.github.io/password-strength/) generátorem. Zároveň je uživateli sdělen status entropie - jestli je na základě počtu bitů heslo slabé/ silné.

<a name="crypt"></a>
### Šifrování a dešifrování souboru s hesly

- (de)šifrování je prováděno AES-256.
- **Dešifrování** - předpokládá se existence databáze `soubor.pass`. Uživatel zadá heslo, to se ověří (musí nastat shoda hashů se souborem `.secret`). Následně se databáze dešifruje a načte se po bytech do paměti `byte[] data`. *(Poté se ještě tato `data` převádí do JSON formátu...) .*
- **Šifrování** - `byte[] data` se zašifrují do souboru `soubor.pass`.
