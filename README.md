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

## Test program
To test the program do:

```bash
sbt test
```

## Requisites
You must have docker in your machine.

## Authors
*   [Ricardo Miranda](https://github.com/ricardomiranda)

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
