#/bin/bash

cds_home=`dirname $PWD`
prog='java'
main_class='com.wangyin.cds.server.Bootstrap'
if ! type $prog >/dev/null 2>&1; then
    if [ -f $JAVA_HOME/bin/$prog ]; then
        prog=$JAVA_HOME/bin/$prog
    else
        echo "can not find java"
        exit 1
    fi
fi
action=$1
object=$2
pass_option="-t $object $action"
class_path="."
for j in $cds_home/lib/*; do
    if [[ `basename $j` == *.jar ]] ; then
        class_path+=":$j"
    fi
done
exec $prog -classpath "$class_path" -Dcds_server_home=$cds_home $main_class $pass_option

