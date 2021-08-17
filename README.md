# solarboat2.0
Website of the Solar Boat Team

## Run in docker container

```sh
docker build -t solarboat .
```
> Build project locally

```sh
docker save -o solarboat_image solarboat
```
> Save locally built image

```sh
docker load -i solarboat_image
```
> Load saved image

```sh
docker run -d --rm --name sb_container -p 81:8080 -v sql:/var/lib/mysql solarboat  
```
> Run image
