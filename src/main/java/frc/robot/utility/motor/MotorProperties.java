package frc.robot.utility.motor;

import java.util.function.Consumer;

public class MotorProperties<T> {
    private final T motor;
    private final Consumer<Direction> directionConsumer;
    private final Consumer<IdleMode> idleModeConsumer;
    private final Consumer<Double> positionConversionFactorConsumer;
    private final Consumer<Double> velocityConversionFactorConsumer;

    private Direction direction;
    private IdleMode idleMode;
    private double positionConversionFactor;
    private double velocityConversionFactor;

    public enum Direction {
        Forward,
        Reversed,
    }

    public enum IdleMode {
        Break,
        Coast,
    }

    private MotorProperties(
        T motor,
        Consumer<Direction> directionConsumer,
        Consumer<IdleMode> idleModeConsumer,
        Consumer<Double> positionConversionFactorConsumer,
        Consumer<Double> velocityConversionFactorConsumer
    ) {
        this.motor = motor;
        this.directionConsumer = directionConsumer;
        this.idleModeConsumer = idleModeConsumer;
        this.positionConversionFactorConsumer = positionConversionFactorConsumer;
        this.velocityConversionFactorConsumer = velocityConversionFactorConsumer;
    }

    public T build() {
        directionConsumer.accept(direction);
        idleModeConsumer.accept(idleMode);
        positionConversionFactorConsumer.accept(positionConversionFactor);
        velocityConversionFactorConsumer.accept(velocityConversionFactor);
        return motor;
    }

    public static <T> MotorProperties<T>.DirectionBuilder create(
        T motor,
        Consumer<Direction> directionConsumer,
        Consumer<IdleMode> idleModeConsumer,
        Consumer<Double> positionConversionFactorConsumer,
        Consumer<Double> velocityConversionFactorConsumer
    ) {
        MotorProperties<T> motorProperties = new MotorProperties<T>(
            motor,
            directionConsumer,
            idleModeConsumer,
            positionConversionFactorConsumer,
            velocityConversionFactorConsumer
        );
        return motorProperties.new DirectionBuilder();
    }

    public class DirectionBuilder {
        public IdleModeBuilder withDirection(Direction direction) {
            MotorProperties.this.direction = direction;
            return new IdleModeBuilder();
        }
    }

    public class IdleModeBuilder {
        public PositionConversionFactorBuilder withIdleMode(IdleMode idleMode) {
            MotorProperties.this.idleMode = idleMode;
            return new PositionConversionFactorBuilder();
        }
    }

    public class PositionConversionFactorBuilder {
        public VelocityConversionFactorBuilder withPositionConversionFactor(double positionConversionFactor) {
            MotorProperties.this.positionConversionFactor = positionConversionFactor;
            return new VelocityConversionFactorBuilder();
        }
    }

    public class VelocityConversionFactorBuilder {
        public MotorProperties<T> withVelocityConversionFactor(double velocityConversionFactor) {
            MotorProperties.this.velocityConversionFactor = velocityConversionFactor;
            return MotorProperties.this;
        }
    }

}
