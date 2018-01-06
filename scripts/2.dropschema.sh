#!/bin/bash
. SENHA_MYSQL
mysql -u root -p$SENHA_MYSQL -h 127.0.0.1 -e "
drop schema IF EXISTS myschema;
create DATABASE myschema CHARACTER SET utf8 COLLATE utf8_bin;
" -v

