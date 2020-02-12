node('master') {

    stage('download from git') {
       echo "Скачиваем репозиторий"
       checkout([$class: 'GitSCM', branches: [[name: "master"]], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: "https://github.com/FSAVE/nginxtest.git"]]])
    }

    stage('ansible nginx') {
        try {
            def result
            timestamps {
                echo "Используем ansible-playbook ${command}.yml"
                result = sh(script: "ansible-playbook -i ./inventories/hosts ./deploy/${command}.yml -b --syntax-check", returnStatus: true)
            }
            if(result==0) {
                echo "Установка выполнена успешно result: ${result}"
            } else {
                echo "Установка провалилась result: ${result}"
                error("Установка провалена, смотри лог!")
            }
        } catch(Ex) {
            echo "${Ex.toString()}"
            currentBuild.result = "FAILED"
            error ("Установка провалена, смотри лог!")
        }
    }
}