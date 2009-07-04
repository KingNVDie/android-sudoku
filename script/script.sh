#!/bin/bash
echo '#!/bin/bash' > test.sh
echo 'for p in $*;' >> test.sh
echo 'do' >> test.sh
echo 'echo "$p";' >> test.sh
echo 'done' >> test.sh
./test.sh $*
