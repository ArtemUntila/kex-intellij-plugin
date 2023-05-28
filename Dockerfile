FROM archlinux as builder

# Download updates + required packages
RUN pacman -Sy --noconfirm \
    git \
    jdk8-openjdk \
    maven

# Clone Kex
RUN git clone -b gui-strategy https://github.com/Artyom-IWT/kex.git

# Build Kex
WORKDIR /kex
RUN mvn package


FROM archlinux as runner
COPY --from=builder /kex /kex
# Download updates + JDK 8
RUN pacman -Sy jdk8-openjdk --noconfirm
# Entrypoint: run Kex
WORKDIR /kex
ENTRYPOINT ["./kex.sh"]