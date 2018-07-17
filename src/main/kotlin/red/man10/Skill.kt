package red.man10

class Skill (
    var name: String,
    var genre: SkillGenre
)

enum class SkillGenre(rawValue: String) {
    Craft("技術スキル"),
    Magic("魔法スキル"),
    Study("学問スキル"),
    Special("スペシャルスキル")
}