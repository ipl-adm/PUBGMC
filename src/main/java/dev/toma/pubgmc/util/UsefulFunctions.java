package dev.toma.pubgmc.util;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class UsefulFunctions {

    public static <T> Predicate<T> alwaysTruePredicate() {
        return p -> true;
    }

    public static double getDistance(Vec3i vec1, Vec3i vec2) {
        return getDistance(vec1.getX(), vec1.getY(), vec1.getZ(), vec2.getX(), vec2.getY(), vec2.getZ());
    }

    public static double getDistance(Vec3d v1, Vec3d v2) {
        return getDistance(v1.x, v1.y, v1.z, v2.x, v2.y, v2.z);
    }

    public static double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Math.sqrt(sqr(x2 - x1) + sqr(y2 - y1) + sqr(z2 - z1));
    }

    public static double sqr(double n) {
        return n * n;
    }

    public static int wrap(int number, int min, int max) {
        return number < min ? min : number > max ? max : number;
    }

    public static float wrap(float number, float min, float max) {
        return number < min ? min : number > max ? max : number;
    }

    public static int getElementCount(Map<?, ? extends Collection<?>> map) {
        int c = 0;
        for(Map.Entry<?, ? extends Collection<?>> entry : map.entrySet()) {
            c += entry.getValue().size();
        }
        return c;
    }

    public static <K, V> V getNonnullFromMap(Map<K, V> map, K key, V defaultValue) {
        V v = map.get(key);
        return v != null ? v : defaultValue;
    }

    public static <T> boolean contains(T[] array, T element) {
        return contains(array, element, (a, b) -> a == b);
    }

    public static <T> boolean contains(T[] array, T element, BiPredicate<T, T> comparing) {
        for(T t : array) {
            if(comparing.test(element, t)) {
                return true;
            }
        }
        return false;
    }

    public static <OBJECT, EXCEPTION extends RuntimeException> OBJECT nonnullOrThrow(OBJECT object, Supplier<EXCEPTION> exceptionSupplier) {
        return validateOrThrow(object, Objects::nonNull, exceptionSupplier);
    }

    public static <OBJECT, EXCEPTION extends RuntimeException> OBJECT validateOrThrow(OBJECT object, Predicate<OBJECT> validator, Supplier<EXCEPTION> exSupplier) {
        if(validator.test(object)) {
            return object;
        } else throw exSupplier.get();
    }

    public static <OBJECT> OBJECT correctAndLog(OBJECT input, Predicate<OBJECT> check, OBJECT out, String message, Logger log) {
        if(check.test(input)) {
            return input;
        } else {
            log.warn("Corrected value {} -> {}: {}", input, out, message);
            return out;
        }
    }

    public static int correctAndLog(int in, int min, int max, Logger log) {
        if(in >= min && in <= max) {
            return in;
        } else {
            if(in < min) {
                log.warn("Corrected value {} -> {}. Input value is too small", in, min);
                return min;
            } else {
                log.warn("Corrected value {} -> {}. Input value is too big", in, max);
                return max;
            }
        }
    }

    public static float correctAndLog(float in, float min, float max, Logger log) {
        if(in >= min && in <= max) {
            return in;
        } else {
            if(in < min) {
                log.warn("Corrected value {} -> {}. Input value is too small", in, min);
                return min;
            } else {
                log.warn("Corrected value {} -> {}. Input value is too big", in, max);
                return max;
            }
        }
    }

    public static double correctAndLog(double in, double min, double max, Logger log) {
        if(in >= min && in <= max) {
            return in;
        } else {
            if(in < min) {
                log.warn("Corrected value {} -> {}. Input value is too small", in, min);
                return min;
            } else {
                log.warn("Corrected value {} -> {}. Input value is too big", in, max);
                return max;
            }
        }
    }
}
