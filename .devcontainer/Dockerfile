FROM maven:3.8-openjdk-18 as build
ARG USERNAME=vscode
ARG USER_UID=1000
ARG USER_GID=$USER_UID
ARG EXTRA_CERT=extra.crt
ENV MAVEN_OPTS="-Djavax.net.ssl.trustStore=/etc/pki/ca-trust/extracted/java/cacerts"
ADD devcontainer.json ${EXTRA_CERT}* ./
RUN if [ ${EXTRA_CERT} != "nope" -a -f ${EXTRA_CERT} ]; then cp ${EXTRA_CERT} /etc/pki/ca-trust/source/anchors/ && update-ca-trust extract && echo "added ${EXTRA_CERT}"; else echo "no extra certificate added"; fi

RUN groupadd --gid $USER_GID $USERNAME \
    && useradd -s /bin/bash --uid $USER_UID --gid $USER_GID -m $USERNAME \
    # [Optional] Add sudo support for the non-root user
    # && apt-get install -y sudo \
    # && echo $USERNAME ALL=\(root\) NOPASSWD:ALL > /etc/sudoers.d/$USERNAME\
    # && chmod 0440 /etc/sudoers.d/$USERNAME \
    #