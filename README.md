# Cafe X

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## Run program
Clone the project to your machine.
Go to project folder.
Run instructions:

```bash
$ sbt assembly
$ docker build -f ./Dockerfile . -t shop
$ docker run -v ~/cafe_x:/opt/app/triggerise -it shop
```

## Test program
To test the program do:

```bash
$ sbt test
```

## Requisites
You must have docker in your machine.

## Authors
*   [Ricardo Miranda](https://github.com/ricardomiranda)

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
