*Czech version [here](README.md).*


# Jpass

A simple password manager written in Java 21 as a credit program in the course [NPRG013](https://d3s.mff.cuni.cz/teaching/nprg013/) at [MFF UK](https://www.mff.cuni.cz/).

The whole program was written in the IDE [IntelliJ IDEA v2023.1.4](https://www.jetbrains.com/idea/) and [documented](docs/) using [javadoc](https://docs.oracle.com/javase/8/docs/technotes/tools/windows/javadoc.html).

## Table of Contents 
- [Features](#features)
- [Prerequirements](#prerequirements)
- [Installation](#installation)
   * [Download jar file from releases](#downjar)
   * [Build from source](#buildsource)
- [Usage](#usage)
   * [Keyboard shortcuts](#key)
   * [Examples](#examples)
- [Used packages](#packages)
- [How does it work](#works)
   * [Passwords](#pass)
   * [Encrypting and decrypting a password file](#crypt)

<a name="pozadavky"></a>
## Features

- Command line controls or simple TUI
- Copy name and password by command or keyboard shortcut
- Clipboard is cleared after copying (default 5s, can be set)
- Database encrypted with AES-256 algorithm + KDF for password with Argon2d algorithm
- Password generator by length
- Warning of weak passwords - Levenshtein on [wordlist](https://github.com/tests-always-included/password-strength/blob/master/data/common-passwords.txt) of common passwords
- In UI mode, up/down arrows iterate through the command history and save the given command to the clipboard
- After the program is properly terminated, the database is encrypted again (with `exit` or `<Ctrl> + D`)

<a name="prerequirements"></a>
## Prerequirements

- Java 21.0.2
- Linux
- Maven (for building from source)

<a name="installation"></a>
## Installation

<a name="downjar"></a>
### Download jar file from releases

The release is at [this](https://github.com/tucnakomet1/jPass/releases/tag/v0.0.1) link. Or it can be downloaded with the following command:
>  `wget https://github.com/tucnakomet1/jPass/releases/download//releases/download/jpass_v0.0.1.tar.gz`

```bash
tar -xvzf jpass_v0.0.1.tar.gz
cd jpass_v0.0.1

chmod +x jpass

./jpass [arg]
```

<a name="buildsource"></a>
### Build from source
```bash
git clone https://github.com/tucnakomet1/jPass.git
cd jPass

mvn package

# run app
java -jar target/pass*.jar
```

<a name="usage"></a>
## Usage

Either the UI version is possible (by calling `./jpass -ui`), or just from the command line by calling `./jpass [arg]`. The commands are the same for both UI and command mode and are listed below:

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
<a name="key"></a>
### Keyboard shortcuts

*Works only in UI mode.*

- `<Ctrl> + B` - Copy the last *username* displayed to the clipboard. *(use `id` or `uname` at least once before this shortcut)*
- `<Ctrl> + P` - Copy the last *password* displayed to the clipboard. *(use `id` or `uname` at least once before this shortcut)*
- `<Ctrl> + D` - Exit the program. The equivalent of the `exit` command.
- `<PgUp>` - Copy the last *used command* to the clipboard. Iterates through history *up*.
- `<PgDown>` - Copy the last *used command* to the clipboard. Iterates through history *down*.

<a name="examples"></a>
### Examples

Add new credentials: 
> `> add Facebook`
```
Enter username: Bob
Enter password: P@sswOrd123
The entropy of the password is: 36.05374780501028 bits
The password is Weak! Crackable by a typical home computer in a week.
This password is, beside entropy, very weak! It's actually a common password - one of top 10000 most used in 2017!
```

Edit credentials: *(in this case we edit the password)*
> `> edit Facebook p`
```
Generate or enter new password (g/e): g
Enter new length of password and if to use specials (in format '<int>, <y|n>'): 26, y
```

Print account info by specific account name: *(including password due to optional argument `y`)*
> `> uname Facebook y`
```
Credentials for account Facebook.
Username: 		Bob
Password: 		**|/,qa(5&kIt|oDQ1vmgE#4b&
```

Print info by specific id: *(without password - no `y` argument)*
> `> ls 8`

```
Credentials for account Account.
Username: 		Adam
Password: 		****************
```

Copy username by specific id: *(copy `Adam` to clipboard)*
> `> c u 8`

<a name="packages"></a>
## Used packages

- **[Bouncy Castle](https://mvnrepository.com/artifact/org.bouncycastle/bcpkix-jdk18on)** - library implementing Argon2
- **[JSON In Java](https://mvnrepository.com/artifact/org.json/json)** - library for working with `.json` files
- **[JNativeHook](https://mvnrepository.com/artifact/com.github.kwhat/jnativehook)** - library containing keyboard listeners

<a name="works"></a>
## How does it work


1. After running `jpass`, the *config* file with all its parameters is first loaded and stored in variables.
*If the config file does not exist, the program prompts the user to create it.*
2. The user has to enter a password (first the hash is verified) and then this password is used to decrypt the password file
3. The password file is decrypted into memory (`byte[] data`) and kept there until the program is terminated. Any changes to the database work with this `data`. It is therefore important to terminate the program properly, otherwise data will be lost (if the database has been modified in any way).

<a name="pass"></a>
### Passwords

- **Common passwords** - When a password is entered, it is verified that the password entered is not one of the TOP  $10~000$ most common passwords of $2017$. This [wordlist](https://github.com/tests-always-included/password-strength/blob/master/data/common-passwords.txt) is browsed.
    - The  [Levenshtein distance](https://www.wikiwand.com/en/Levenshtein_distance) of the password is also checked. If it is less than $4$ and a match occurs, the password is again considered very weak and the user is notified.
- **Entropy** - for each entered password its [entropy](https://www.wikiwand.com/en/Entropy_(information_theory)) is calculated in bits. I was inspired by [this](http://tests-always-included.github.io/password-strength/) generator. At the same time, the entropy status is reported to the user - whether the password is weak/strong (based on the number of bits).

<a name="crypt"></a>
### Encrypting and decrypting a password file

- Decryption/ encryption is done by AES-256.
- **Decryption** - The existence of encrypted password file `name.pass` is required. The user enters a password and then verifies (the hashes must match the `.secret` file). The database is then decrypted and loaded byte by byte into `byte[] data` memory. *(Then this `data` is converted to JSON format...) .*
- **Encryption** - `byte[] data` is encrypted into the file `name.pass`.