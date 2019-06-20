def label = "ci-cd-${UUID.randomUUID().toString()}"

podTemplate(label: label,
    serviceAccount: 'helm',
    containers: [
        containerTemplate(name: 'maven', image: 'maven:3.6-jdk-11-slim', ttyEnabled: true, command: 'cat'),
        containerTemplate(
            name: 'docker',
            image: 'docker',
            command: 'cat',
            ttyEnabled: true,
            envVars: [
                secretEnvVar(key: 'REGISTRY_USERNAME', secretName: 'registry-credentials', secretKey: 'username'),
                secretEnvVar(key: 'REGISTRY_PASSWORD', secretName: 'registry-credentials', secretKey: 'password')
            ]
        ),
        containerTemplate(name: 'helm', image: 'alpine/helm:2.11.0', command: 'cat', ttyEnabled: true)
    ],
    volumes: [
        hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock')
    ]) {

    node(label) {
        stage('Checkout project') {
            def repo = checkout scm
            def dockerImage = "ci-cd-guide/spring-boot"
            def dockerTag = "${repo.GIT_BRANCH}-${repo.GIT_COMMIT}"
            def registryUrl = "registry.home.koudingspawn.de"
            def helmName = "spring-boot-${repo.GIT_BRANCH}"
            def url = "${helmName}.home.koudingspawn.de"
            def certVaultPath = "certificates/*.home.koudingspawn.de"
            
            container('maven') {
                stage('Build project') {
                    sh 'mvn -B clean install'
                }
            }

            container('docker') {
                stage('Build docker') {
                    sh "docker build -t ${registryUrl}/${dockerImage}:${dockerTag} ."
                    sh "docker login ${registryUrl} --username \$REGISTRY_USERNAME --password \$REGISTRY_PASSWORD"
                    sh "docker push ${registryUrl}/${dockerImage}:${dockerTag}"
                    sh "docker rmi ${registryUrl}/${dockerImage}:${dockerTag}"
                }
            }

            if ("${repo.GIT_BRANCH}" == "master") {
                container('helm') {
                    stage("Deploy") {
                        sh("""helm upgrade --install ${helmName} --namespace='${helmName}' \
                                --set ingress.enabled=true \
                                --set ingress.hosts[0]='${url}' \
                                --set ingress.annotations.\"kubernetes\\.io/ingress\\.class\"=nginx \
                                --set ingress.tls[0].hosts[0]='${url}' \
                                --set ingress.tls[0].secretName='tls-${url}' \
                                --set ingress.tls[0].vaultPath='${certVaultPath}' \
                                --set ingress.tls[0].vaultType='CERT' \
                                --set image.tag='${dockerTag}' \
                                --set image.repository='${registryUrl}/${dockerImage}' \
                                --wait \
                               ./helm""")
                    }
                }
            }
        }
    }
}

