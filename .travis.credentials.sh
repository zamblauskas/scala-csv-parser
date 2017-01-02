#!/bin/bash

set -euo pipefail

mkdir ~/.bintray/

cat <<EOF > $HOME/.bintray/.credentials
realm = Bintray API Realm
host = api.bintray.com
user = $BINTRAY_USER
password = $BINTRAY_PASSWORD
EOF

