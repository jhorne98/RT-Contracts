#!/usr/bin/env bash
# Create templates for default infrastructure files
# NOTE: This will not create everything necessary to run the
# application, and will require additions

a=$(echo -e $1 | awk '{print tolower($0)}')

#TODO database table formatting

# Create the Repository layer files
mkdir src/main/kotlin/com/radiotelescope/repository/$a

touch src/main/kotlin/com/radiotelescope/repository/$a/I$1Repository.kt
sed "s/{NAME_CAMEL}/$a/g; s/{NAME_FIRST_CAPITAL}/$1/g" template/repository/ITemplateRepository.txt > src/main/kotlin/com/radiotelescope/repository/$a/I$1Repository.kt

touch src/main/kotlin/com/radiotelescope/repository/$a/$1.kt
sed "s/{NAME_CAMEL}/$a/g; s/{NAME_FIRST_CAPITAL}/$1/g" template/repository/Template.txt > src/main/kotlin/com/radiotelescope/repository/$a/$1.kt

# Create the Contracts layer files
mkdir src/main/kotlin/com/radiotelescope/contracts/$a

touch src/main/kotlin/com/radiotelescope/contracts/$a/$1Factory.kt
sed "s/{NAME_CAMEL}/$a/g; s/{NAME_FIRST_CAPITAL}/$1/g" template/contracts/TemplateFactory.txt > src/main/kotlin/com/radiotelescope/contracts/$a/$1Factory.kt

touch src/main/kotlin/com/radiotelescope/contracts/$a/Base$1Factory.kt
sed "s/{NAME_CAMEL}/$a/g; s/{NAME_FIRST_CAPITAL}/$1/g" template/contracts/BaseTemplateFactory.txt > src/main/kotlin/com/radiotelescope/contracts/$a/Base$1Factory.kt

touch src/main/kotlin/com/radiotelescope/contracts/$a/User$1Wrapper.kt
sed "s/{NAME_CAMEL}/$a/g; s/{NAME_FIRST_CAPITAL}/$1/g" template/contracts/UserTemplateWrapper.txt > src/main/kotlin/com/radiotelescope/contracts/$a/User$1Wrapper.kt

# Add all to git
git add .