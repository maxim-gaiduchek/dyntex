import pytest
import subprocess

def test_pylint():
    """
    Test that runs pylint on the code and checks if it passes.
    """
    pylint_output = subprocess.run(['pylint', 'src/*'], capture_output=True, text=True)
    assert pylint_output.returncode == 0, f"pylint found issues:\n{pylint_output.stdout}"
