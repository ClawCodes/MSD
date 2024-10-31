import { View, Text, TextInput } from 'react-native';

function InputWidget({ labelName, value, onChangeText, placeHolderValue }) {
    return (
        <View style={{ marginBottom: 10 }}>
            <Text>{labelName}</Text>
            <TextInput
                style={{ borderColor: 'gray', borderWidth: 1, padding: 5 }}
                value={value}
                onChangeText={onChangeText}
                placeholder={placeHolderValue}
            />
        </View>
    );
}

export default InputWidget;