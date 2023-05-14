FROM archlinux as builder

# Download updates + required packages
RUN pacman -Sy --noconfirm \
    git \
    jdk8-openjdk \
    maven

# Clone Kex
WORKDIR /home
RUN git clone https://github.com/vorpal-research/kex.git

# Build Kex
WORKDIR /home/kex
RUN mvn package


FROM archlinux as runner
COPY --from=builder /home/kex /home/kex
# Download updates + JDK 8
RUN pacman -Sy jdk8-openjdk --noconfirm
# Entrypoint: run Kex
WORKDIR /home/kex
ENTRYPOINT ["./kex.sh"]