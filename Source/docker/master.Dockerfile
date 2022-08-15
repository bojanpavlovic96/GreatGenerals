# used to check what is in build context 
# (what is not ignored by the .dockerignore)

# @host $ docker build --tag gg/b_context --file docker/master.Dockerfile --no-cache .
# @host $ docker run -it --rm --entrypoint /bin/sh gg/b_context

# @conatienr: /build_context $ du -hs ./* 

FROM busybox

WORKDIR /build_context

COPY . . 
