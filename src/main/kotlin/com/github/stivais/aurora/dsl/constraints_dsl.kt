@file:Suppress("unused")

package com.github.stivais.aurora.dsl

import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.Constraints
import com.github.stivais.aurora.constraints.Positions
import com.github.stivais.aurora.constraints.impl.measurements.Percent
import com.github.stivais.aurora.constraints.impl.measurements.Pixel
import com.github.stivais.aurora.constraints.impl.measurements.Undefined
import com.github.stivais.aurora.constraints.impl.operational.Additive
import com.github.stivais.aurora.constraints.impl.operational.Multiplicative
import com.github.stivais.aurora.constraints.impl.operational.Subtractive
import com.github.stivais.aurora.constraints.impl.positions.Alignment
import com.github.stivais.aurora.constraints.impl.size.Bounding
import com.github.stivais.aurora.constraints.impl.size.Copying
import com.github.stivais.aurora.constraints.impl.size.Fill

//
// This file contains functions to make
// using certain constraints simpler/cleaner
//

/**
 * Creates a pixel constraint from a number.
 */
inline val Number.px: Pixel
    get() = Pixel(this.toFloat())

/**
 * Creates a percent constraint from a number (0-100).
 */
inline val Number.percent: Percent
    get() = Percent(this.toFloat() / 100f)

/**
 * Creates an [Alignment.Right] position from an existing [position][Constraint.Position]
 */
inline val Constraint.Position.alignRight: Alignment.Right
    get() = Alignment.Right(this)

/**
 * Creates an [Alignment.Center] position from an existing [position][Constraint.Position]
 */
inline val Constraint.Position.alignCenter: Alignment.Center
    get() = Alignment.Center(this)

/**
 * Creates an [Alignment.Opposite] position from an existing [position][Constraint.Position]
 */
inline val Constraint.Position.alignOpposite: Alignment.Opposite
    get() = Alignment.Opposite(this)

/**
 * Shortened version of [Positions], for a much more appealing look.
 */
fun at(
    x: Constraint.Position = Undefined,
    y: Constraint.Position = Undefined,
) = Positions(x, y)

/**
 * Creates a [Constraints] instance, where x and y are [Undefined].
 */
fun size(
    w: Constraint.Size = Undefined,
    h: Constraint.Size = Undefined,
) = Constraints(Undefined, Undefined, w, h)

/**
 * Literally just [Constraints].
 */
fun constrain(
    x: Constraint.Position = Undefined,
    y: Constraint.Position = Undefined,
    w: Constraint.Size = Undefined,
    h: Constraint.Size = Undefined,
) = Constraints(x, y, w, h)

/**
 * Creates a [Constraints] instance,
 * where x and y are [0.px][Pixel] and width and height are [Copying].
 */
fun copies() = Constraints(Pixel.ZERO, Pixel.ZERO, Copying, Copying)

/**
 * Creates a [Constraints] instance,
 * where x and y [amount.px][Pixel] and width and height are [Copying] - [amount][Subtractive]
 */
fun indent(amount: Float): Constraints {
    if (amount == 0f) return copies()

    val indentPos = amount.px
    val indentSize = Fill - indentPos
    return Constraints(indentPos, indentPos, indentSize, indentSize)
}

/**
 * Creates a [Constraints] instance,
 * where the sizes are [Bounding] + [padding], with optional x and y positions.
 */
fun bounds(
    x: Constraint.Position = Undefined,
    y: Constraint.Position = Undefined,
    padding: Constraint.Size? = null
): Constraints {
    if (padding == null) return constrain(x, y, Bounding, Bounding)

    val size = Bounding + padding
    return constrain(x, y, size, size)
}

/**
 * Adds one constraint to another to create an [Additive] constraint
 */
operator fun Constraint.plus(other: Constraint) = Additive(this, other)


/**
 * Adds one constraint to another to create a [Subtractive] constraint
 */
operator fun Constraint.minus(other: Constraint) = Subtractive(this, other)


/**
 * Adds one constraint to another to create a [Multiplicative] constraint
 */
operator fun Constraint.times(other: Constraint) = Multiplicative(this, other)
