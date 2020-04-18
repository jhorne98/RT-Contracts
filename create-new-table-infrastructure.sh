#!/usr/bin/env bash
# Create templates for

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

# Add all to git
git add .

# Create the Contracts layer files
cd ../..
cd contracts
mkdir $a
cd $a

touch $1Factory.kt
echo -e "package com.radiotelescope.contracts.$a" >> $1Factory.kt
echo -e >> $1Factory.kt
echo -e "import com.radiotelescope.repository.$a.$1" >> $1Factory.kt
echo -e >> $1Factory.kt
echo -e "/**" >> $1Factory.kt
echo -e " * Abstract factory interface with methods for all [$1] operations" >> $1Factory.kt
echo -e " */" >> $1Factory.kt
echo -e "interface $1Factory {" >> $1Factory.kt
echo -e "}" >> $1Factory.kt

touch Base$1Factory.kt
echo -e "package com.radiotelescope.contracts.$a" >> Base$1Factory.kt
echo -e >> Base$1Factory.kt
echo -e "import com.radiotelescope.repository.$a.I$1Repository" >> Base$1Factory.kt
echo -e "import com.radiotelescope.repository.$a.$1" >> Base$1Factory.kt
echo -e >> Base$1Factory.kt
echo -e "/**" >> Base$1Factory.kt
echo -e " * Base concrete implementation of the [$1Factory] interface" >> Base$1Factory.kt
echo -e " *" >> Base$1Factory.kt
echo -e " * @param videoFileRepo the [I$1Repository] interface" >> Base$1Factory.kt
echo -e " */" >> Base$1Factory.kt
echo -e "class Base$1Factory(" >> Base$1Factory.kt
echo -e "\t\tprivate val ${a}Repo: I$1Repository" >> Base$1Factory.kt
echo -e ") : $1Factory {" >> Base$1Factory.kt
echo -e "}" >> Base$1Factory.kt

touch User$1Wrapper.kt
echo -e "package com.radiotelescope.contracts.$a" >> User$1Wrapper.kt
echo -e >> User$1Wrapper.kt
echo -e "import com.radiotelescope.security.UserContext" >> User$1Wrapper.kt
echo -e >> User$1Wrapper.kt
echo -e "/**" >> User$1Wrapper.kt
echo -e " * Wrapper that takes a [$1Factory] and is responsible for all" >> User$1Wrapper.kt
echo -e " * user role validations for the $1 Entity" >> User$1Wrapper.kt
echo -e " *" >> User$1Wrapper.kt
echo -e " * @property context the [UserContext] interface" >> User$1Wrapper.kt
echo -e " * @property factory the [${1}Factory interface" >> User$1Wrapper.kt
echo -e " */" >> User$1Wrapper.kt
echo -e "class User$1Wrapper(" >> User$1Wrapper.kt
echo -e "\t\tprivate val context: UserContext," >> User$1Wrapper.kt
echo -e "\t\tprivate val factory: ${1}Factory" >> User$1Wrapper.kt
echo -e ") {" >> User$1Wrapper.kt
echo -e "}" >> User$1Wrapper.kt

# Add all to git
git add .