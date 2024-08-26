package de.php_perfect.intellij.ddev.icons

import com.intellij.ui.IconManager.getIcon
import javax.swing.Icon

object DdevIntegrationIcons {
    @JvmField
    val DdevLogoColor: Icon =
        getInstance.getInstance().getIcon("/icons/ddevLogoColor.svg", DdevIntegrationIcons::class.java.getClassLoader())
    @JvmField
    val DdevLogoMono: Icon =
        getInstance.getInstance().getIcon("/icons/ddevLogoGrey.svg", DdevIntegrationIcons::class.java.getClassLoader())
}
