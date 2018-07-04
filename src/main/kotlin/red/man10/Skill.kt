package red.man10

class Skill (
    var name: String,
    var genre: SkillGenre// 0.Craft 1.Magic 2.Study
)

enum class SkillGenre {
    Craft(),
    Magic(),
    Study()
}