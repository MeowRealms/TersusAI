package ink.ptms.tersusai.rule

import com.google.common.collect.Lists
import ink.ptms.tersusai.inject.TersusInjector
import org.bukkit.Location
import taboolib.common.util.randomDouble
import taboolib.common5.compileJS
import taboolib.common5.format

import javax.script.CompiledScript
import javax.script.SimpleBindings

/**
 * @author sky
 * @since 2019-08-23 16:42
 */
class Rule {

    var condition: CompiledScript? = null
        private set
    var inhibit: Int = 0
        private set
    var affect: RuleAffect? = null
        private set
    var logger: List<String>? = null
        private set

    constructor(condition: String, inhibit: Int, affect: String) {
        this.condition = condition.compileJS()
        this.inhibit = inhibit
        this.affect = RuleAffect.fromStr(affect)
        this.logger = Lists.newArrayList()
    }

    constructor(condition: String, inhibit: Int, affect: String, logger: List<String>) {
        this.condition = condition.compileJS()
        this.inhibit = inhibit
        this.affect = RuleAffect.fromStr(affect)
        this.logger = logger
    }

    fun check(count: Int, p: Double, affect: RuleAffect): Boolean {
        if (affect !== this.affect) {
            return false
        }
        val simpleBindings = SimpleBindings()
        simpleBindings["\$random"] = randomDouble()
        simpleBindings["\$tps"] = TersusInjector.getTps()
        simpleBindings["\$count"] = count
        simpleBindings["\$p"] = p
        try {
            return condition!!.eval(simpleBindings).toString().toBoolean()
        } catch (t: Throwable) {
            t.printStackTrace()
        }
        return false
    }

    fun log(count: Int, p: Double, location: Location) {
        logger!!.map { line ->
            line
                .replace("\$tps", TersusInjector.getTps()[0].format().toString())
                .replace("\$count", count.toString())
                .replace("\$p", p.toString())
                .replace("\$location", location.world!!.name + ":" + location.blockX + "," + location.blockY + "," + location.blockZ)
        }.forEach { println(it) }
    }
}
