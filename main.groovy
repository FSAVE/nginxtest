node('master') {
    stage('ansible nginx') {
        try {
            def result
            timestamps{
                echo "Используем ansible-playbook ${command}.yml"
                result = sh(script: "ansible-playbook -i ./inventories/hosts ./deploy/${command}.yml -b", returnStatus: true)
            }
            if(result==0) {
                echo "Установка выполнена успешно result: ${result}"
            } else {
                echo "Установка провалилась result: ${result}"
        }
        catch(Ex) {
          echo "${Ex.toString()}"
          currentBuild.result = "FAILED"
          error ("Установка провалена, смотри лог!")
        }

    }
}