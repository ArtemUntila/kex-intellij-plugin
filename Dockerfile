FROM archlinux

# Download database files
RUN pacman -Sy

# Java 8
RUN pacman -S jdk8-openjdk --noconfirm

# Git
RUN pacman -S git --noconfirm

# Maven
RUN pacman -S maven --noconfirm

# Clone Kex
WORKDIR /home
RUN git clone https://github.com/vorpal-research/kex.git

# Build Kex
WORKDIR /home/kex
RUN mvn package

# Java latest
RUN archlinux-java unset
RUN pacman -S jdk-openjdk --noconfirm

# Entrypoint (execute Kex)
ENTRYPOINT ["/home/kex/kex.sh"]
