// def call(String project, String ImageTag, String hubUser){
//     withCredentials([usernamePassword(
//             credentialsId: "docker",
//             usernameVariable: "USER",
//             passwordVariable: "PASS"
//     )]) {
//         sh "docker login -u '$USER' -p '$PASS'"
//     }
//     sh "docker image push ${hubUser}/${project}:${ImageTag}"
//     sh "docker image push ${hubUser}/${project}:latest"   
// }


def call(String aws_account_id, String region, String ecr_repoName) {
    withCredentials([
        string(credentialsId: 'AWS_ACCESS_KEY_ID', variable: 'AWS_ACCESS_KEY_ID'),
        string(credentialsId: 'AWS_SECRET_KEY_ID', variable: 'AWS_SECRET_ACCESS_KEY')
    ]) {
        withEnv([
            "AWS_DEFAULT_REGION=${region}",
            "ECR_REGISTRY=${aws_account_id}.dkr.ecr.${region}.amazonaws.com",
            "REPO_NAME=${ecr_repoName}"
        ]) {
            sh '''
                aws ecr get-login-password --region ${AWS_DEFAULT_REGION} | docker login --username AWS --password-stdin ${ECR_REGISTRY}
                
                docker push ${ECR_REGISTRY}/${REPO_NAME}:latest
            '''
        }
    }
}
