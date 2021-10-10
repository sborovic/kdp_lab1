![Java](https://img.shields.io/badge/java-JavaSE_15-orange?logo=java)
![Eclipse](https://img.shields.io/badge/eclipse-blue.svg?logo=eclipseide)
![GitHub](https://img.shields.io/github/license/sborovic/kdp_lab1)

# Конкурентно и дистрибуирано програмирање (13С11КДП) - Лабораторијска вежба бр. 1

![Screen recording of the application in action](https://github.com/sborovic/kdp_lab1/blob/master/kdp_lab1.gif)

## Тема вежбе
Потребно је написати Јава апликацију која проналази број још увек живих
глумаца рођених у некој декади.

## Поставка вежбе
Решење мора бити максимално конкурентно и
отпорно на прекиде. Дозвољено је додавати нове редове у дати костур
апликације и додавати аргументе конструкторима, али није дозвољено
брисати постојеће редове.
Делови апликације:
* Класа **Producer** обрађује улазну датотеку са особама и информације о свакој
појединачној особи убацује у дељени објекат за даљу обраду. Ова класа обраду
обавља у свом току контроле. Подаци о особама се читају ред по ред из архиве
са именима _(name.basic.tsv.gz)_.
* Класа **Consumer** обрађује информације о добијеној особи и резултате обраде на
крају прослеђује на даљу обраду. Ова класа обраду обавља у свом току
контроле. Класа чита информације о некој особи из дељеног објекта. Ако је
особа глумац/глумица _(primaryProfession)_ која је и даље жив/а (_deathYear_ није
\N), локално се чувају информације о обрађеним особама које су рођене
_(birthYear)_ у некој декади. Особа може имати више професија
_(primaryProfession)_, па се међу њима мора наћи _actor_ или _actress_ да би се особа
сматрала глумцем/глумицом. На сваких N (задаје се) обрађених особа које један
**Consumer** обради, ажурира информацију о свом броју обрађених особа
користећи посебан дељени објекат и прослеђује их на даље сумирање
користећи следећи дељени објекат. Када не буде било више особа за обраду,
**Consumer** прослеђује информације даље на сумирање и такође ажурира
информацију о броју обрађених особа.
* Класа **Combiner** на основу прикупљених парцијалних података формира коначну
листу о броју глумаца/глумица рођених у одређеној декади. Ова класа обраду
обавља у свом току контроле. Класа преузима податке из дељеног објекта и
након завршене обраде резултат убацује у следећи дељени објекат.
* Класа **Printer** периодично (задаје се) штампа информације о броју особа коју је
обрадила свака појединачна класа **Consumer**, све док се не прикупе коначни
подаци и на крају штампа њих штампа на основу података које је доставила
класа **Combiner**. Ова класа обраду обавља у свом току контроле. Излаз
апликације је листа која садржи декаду и број живих глумаца у тој декади.
Декадом сматрати период нпр. 1960-1969, 1970-1979, итд.
* Класа **Test** тестира овај систем. Класа у својој почетној **main** методи креира све
потребне дељене објекте, покреће једну инстанцу класе **Producer**, већи
(подесив) број инстанци класе **Consumer**, једну инстанцу класе **Combiner** и једну
инстанцу класе **Printer**. Након комплетно завршене обраде, штампа колико је
износило укупно време обраде за дати број нити које раде обраду.


## Покретање

Покренути класу **Main**. Потребно је да архива са именима из које се чита буде доступна класи **Main**. 

## Лиценца
[MIT](https://github.com/sborovic/kdp_lab1/blob/master/LICENSE)