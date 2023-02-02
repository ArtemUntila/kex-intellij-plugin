FROM archlinux as builder

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

FROM openjdk:jre-slim
COPY --from=builder /home/kex /kex
RUN mv /kex/kex-runner/target/kex-runner-*-jar-with-dependencies.jar /kex/kex-runner/target/kex-runner.jar
WORKDIR /kex
ENTRYPOINT ["java", \
    "-Xmx8g", \
    "-Djava.security.manager", \
    "-Djava.security.policy==kex.policy", \
    "-Dlogback.statusListenerClass=ch.qos.logback.core.status.NopStatusListener", \
    "-jar", "kex-runner/target/kex-runner.jar" \
]

