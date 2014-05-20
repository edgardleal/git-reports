#!/bin/bash


################################################################## 
#                                                                # 
#                                                                # 
#                                                                # 
# @author Edgard Leal                                            # 
#                                                                # 
################################################################## 


declare -r h=$HOME
app_home="$( cd "$( dirname "${BASH_SOURCE[0]}" )/" && pwd )"


export app_home=$app_home
declare -r profile="homol"

function showlog
{
  vim $app_home/log/socialtime.log
}


classpath="./:$app_home/lib/*"

function run
{

  if [ "$isruning" != "" ]; then 
    echo "o programa ja esta em execucao"
    exit 0
  else
     local command="java -classpath \"$classpath\" com.github.gitreport.gui.Main \
          -Xmx1024m \
          -Xms256m \
          -Dapp_home=$app_home 2>&1 "
     eval $command
  fi
}

     run
