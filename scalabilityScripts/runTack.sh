valuate () {
command="java -jar tack.jar "$1" "$2" "$3"; exit \$res; echo \$res >>" $outputfile

echo $command
#timeout 5s bash -c 'eval '+$command
#eval $command

timeout 6h $command

cp tmp.zot TMP/tmp$property$i$bound.zot

}


i=$1
property=$2
bound=$3
outputfile=$4
modelname="fischer/fischer_input_"
modelname=$modelname$i".ta"
propertyname="fischer/fischer_input_P"
propertyname=$propertyname$property".mitli"

valuate $modelname $propertyname $bound
