# Triggerise shop

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/09acde1e82d246679e3d89622e5c0c68)](https://www.codacy.com/manual/mail_62/triggerise-shop?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=ricardomiranda/triggerise-shop&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/09acde1e82d246679e3d89622e5c0c68)](https://www.codacy.com/manual/mail_62/triggerise-shop?utm_source=github.com&utm_medium=referral&utm_content=ricardomiranda/triggerise-shop&utm_campaign=Badge_Coverage)

## Run program
Clone the project to your machine.
Go to project folder.
Run instructions:

```bash
sbt assembly
docker build -f ./Dockerfile . -t shop
docker run -v ~/shop:/opt/app/triggerise -it shop
```

## User interaction
When running the program you can create a shopping list typping any sequence of 
HAT, HOODIE and TICKET, separated by spaces. Bellow a session is shown as an example:

```bash
Menu options are:
 1 -> Calc new shopping kart
 2 -> Exit program
 3 -> This help\n
Please enter list of products separated by commas.
items: TICKET, HOODIE, TICKET
total: 25.00
What do you want to do now? (1 -> Continue, 2 -> Exit, 3 -> Help)
1
Please enter list of products separated by commas.
items: TICKET, HOODIE, HAT
total: 32.50
What do you want to do now? (1 -> Continue, 2 -> Exit, 3 -> Help)
1
Please enter list of products separated by commas.
items: HOODIE, HOODIE, HOODIE, TICKET, HOODIE
total: 81.00
What do you want to do now? (1 -> Continue, 2 -> Exit, 3 -> Help)
1
Please enter list of products separated by commas.
items: TICKET, HOODIE, TICKET, TICKET, HAT, HOODIE, HOODIE
total: 74.50
What do you want to do now? (1 -> Continue, 2 -> Exit, 3 -> Help)
3
Menu options are:
 1 -> Calc new shopping kart
 2 -> Exit program
 3 -> This help\n
What do you want to do now? (1 -> Continue, 2 -> Exit, 3 -> Help)
2
```

## Test program
To test the program do:

```bash
sbt test
```

## Requisites
You must have docker, JDK and SBT installed in your machine.

## Authors
*   [Ricardo Miranda](https://github.com/ricardomiranda)

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
