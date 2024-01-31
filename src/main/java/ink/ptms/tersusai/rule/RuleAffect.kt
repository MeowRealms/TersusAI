package ink.ptms.tersusai.rule

import java.util.*

/**
 * @author sky
 * @since 2019-08-23 16:41
 */
enum class RuleAffect {

    GROUP, GLOBAL, SINGLE, NONE;

    companion object {

        fun fromStr(`in`: String): RuleAffect {
            return try {
                valueOf(`in`.uppercase(Locale.getDefault()))
            } catch (ignore: Throwable) {
                NONE
            }
        }
    }
}
