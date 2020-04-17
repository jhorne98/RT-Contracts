#!/usr/bin/env bash

a=$(echo -e $1 | awk '{print tolower($0)}')

# Create the Repository layer files
cd src/main/kotlin/com/radiotelescope/repository
mkdir $a
cd $a

touch $1.kt
echo -e "package com.radiotelescope.repository.$a" >> $1.kt
echo -e >> $1.kt
echo -e "import java.util.*" >> $1.kt
echo -e "import javax.persistence.*" >> $1.kt
echo -e >> $1.kt
echo -e "/**" >> $1.kt
echo -e " * Entity Class representing a $1 for the web-application" >> $1.kt
echo -e " *" >> $1.kt
echo -e " * This Entity correlates to the $a SQL table" >> $1.kt
echo -e " */" >> $1.kt
echo -e "@Entity" >> $1.kt
echo -e "@Table(name = \"$a\")" >> $1.kt
echo -e "data class $1 () {" >> $1.kt
echo -e "\t@Id" >> $1.kt
echo -e "\t@GeneratedValue(strategy = GenerationType.IDENTITY)" >> $1.kt
echo -e "\t@Column(name = \"id\", unique = true, nullable = false)" >> $1.kt
echo -e "\tvar id: Long = 0" >> $1.kt
echo -e >> $1.kt
echo -e "\t@Column(name = \"insert_timestamp\", nullable = false)" >> $1.kt
echo -e "\tvar recordCreatedTimestamp: Date = Date()" >> $1.kt
echo -e >> $1.kt
echo -e "\t@Column(name = \"update_timestamp\", nullable = true)" >> $1.kt
echo -e "\tvar recordUpdatedTimestamp: Date = Date()" >> $1.kt
echo -e "}" >> $1.kt

touch I$1Repository.kt
echo -e "package com.radiotelescope.repository.$a" >> I$1Repository.kt
echo -e >> I$1Repository.kt
echo -e "import org.springframework.stereotype.Repository" >> I$1Repository.kt
echo -e >> I$1Repository.kt
echo -e "@Repository" >> I$1Repository.kt
echo -e "interface I$1Repository" >> I$1Repository.kt

# Add to git
git add .