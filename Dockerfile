FROM archlinux as builder

# Download updates + build packages
RUN pacman -Syu --noconfirm \
    git \
    jdk8-openjdk \
    maven

# Clone Kex
RUN git clone https://github.com/vorpal-research/kex.git

# Build Kex
WORKDIR /kex
RUN mvn package


FROM archlinux as runner
COPY --from=builder /kex /kex
# Download updates + run packages
RUN pacman -Syu --noconfirm \
    jdk11-openjdk \
    python
# Entrypoint: run Kex
WORKDIR /kex
ENTRYPOINT ["python", "./kex.py"]