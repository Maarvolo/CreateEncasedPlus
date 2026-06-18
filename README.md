<h1 align="center"> Create Encased Plus<br>

</h1>

<h2 align="center">This is a fork of <a href="https://github.com/iglee42/CreateCasing">Create Encased</a> that aims to separate encased machines recipes for modpack makers</h2>
<p align="center">Create Encased is an addon for <a href="https://github.com/Creators-of-Create/Create/">Create</a></p>
<p align="center">The original addon allows the use of all casings in shafts, cogwheels, pipes and machines</p>

<h3>Recipes:</h3>
<p>create_encasedplus:casing_pressing :</p>

```groovy
{
  "type": "create_encasedplus:casing_pressing",
  "press": "create_encasedplus:copper_press",
  "ingredients": [
    {
      "item": "minecraft:potato"
    }
  ],
  "results": [
    {
      "id": "minecraft:redstone_block"
    }
  ]
}

```
<p>create_encasedplus:casing_mixing :</p>

```groovy
{
  "type": "create_encasedplus:casing_mixing",
  "mixer": "create_encasedplus:copper_mixer",
  "ingredients": [
    {
      "item": "minecraft:diamond"
    }
  ],
  "results": [
    {
      "id": "minecraft:apple"
    }
  ]
}
```
<p align="center"><b>"press"</b> and <b>"mixer"</b> can be omitted to allow every encased machines to process the recipe</p>

<h6 align="center">Original mod by <a href="https://github.com/iglee42">iglee42</a></h6>
<h6 align="center">Original idea by <a href="https://www.youtube.com/MrMLDEG">MLDEG</a></h6>