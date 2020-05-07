#!/usr/bin/env bash
# Create default infrastructure files using template files.
# NOTE: This will not create everything necessary to run the
# application, and will require additions.

# Input should be word-capitalized, i.e. "SampleInfrastructureAddition".
# This input will be used to generate a camel-case string (sampleInfrastructureAddition)
# and a database table formatted string (sample_infrastructure_addition)

# Generate camel-case string
camel=$(echo -e $1 | awk '{print tolower(substr($0,1,1)) substr($0,2);}')

# Generate database table string
table=$(echo -e $camel | awk '{t = $0; gsub("[[:upper:]]","_&",t); print tolower(t)}')

# Create the Repository layer files
mkdir src/main/kotlin/com/radiotelescope/repository/$camel

touch src/main/kotlin/com/radiotelescope/repository/$camel/I$1Repository.kt
sed "s/{NAME_CAMEL}/$camel/g; s/{NAME_FIRST_CAPITAL}/$1/g" template/repository/ITemplateRepository.txt > src/main/kotlin/com/radiotelescope/repository/$camel/I$1Repository.kt

touch src/main/kotlin/com/radiotelescope/repository/$camel/$1.kt
sed "s/{NAME_CAMEL}/$camel/g; s/{NAME_FIRST_CAPITAL}/$1/g; s/{NAME_TABLE}/$table/g" template/repository/Template.txt > src/main/kotlin/com/radiotelescope/repository/$camel/$1.kt

# Create the Contracts layer files
mkdir src/main/kotlin/com/radiotelescope/contracts/$camel

touch src/main/kotlin/com/radiotelescope/contracts/$camel/$1Factory.kt
sed "s/{NAME_CAMEL}/$camel/g; s/{NAME_FIRST_CAPITAL}/$1/g" template/contracts/TemplateFactory.txt > src/main/kotlin/com/radiotelescope/contracts/$camel/$1Factory.kt

touch src/main/kotlin/com/radiotelescope/contracts/$camel/Base$1Factory.kt
sed "s/{NAME_CAMEL}/$camel/g; s/{NAME_FIRST_CAPITAL}/$1/g" template/contracts/BaseTemplateFactory.txt > src/main/kotlin/com/radiotelescope/contracts/$camel/Base$1Factory.kt

touch src/main/kotlin/com/radiotelescope/contracts/$camel/User$1Wrapper.kt
sed "s/{NAME_CAMEL}/$camel/g; s/{NAME_FIRST_CAPITAL}/$1/g" template/contracts/UserTemplateWrapper.txt > src/main/kotlin/com/radiotelescope/contracts/$camel/User$1Wrapper.kt

# Add all to git
git add src/.